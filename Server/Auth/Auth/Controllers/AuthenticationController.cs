using System.Security.Claims;
using Auth.Models;
using Auth.Models.Responses;
using Auth.Services.Authenticators;
using Auth.Services.Repositories.RefreshTokenRepositories;
using Auth.Services.Repositories.UserRepositories;
using Auth.Services.TokenValidator;
using Auth.Models.Requests;
using Auth.Services.PasswordHashers;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;

namespace Auth.Controllers
{
    public class AuthenticationController : Controller
    {
        private readonly IUserRepository _userRepository;
        private readonly IPasswordHasher _passwordHasher;
        private readonly Authenticator _authenticator;
        private readonly RefreshTokenValidator _refreshTokenValidator;
        private readonly IRefreshTokenRepository _refreshTokenRepository;


        public AuthenticationController(IUserRepository userRepository, IPasswordHasher passwordHasher,
            Authenticator authenticator, RefreshTokenValidator refreshTokenValidator, IRefreshTokenRepository refreshTokenRepository)
        {
            _userRepository = userRepository;
            _passwordHasher = passwordHasher;
            _authenticator = authenticator;
            _refreshTokenValidator = refreshTokenValidator;
            _refreshTokenRepository = refreshTokenRepository;
        }

        [HttpPost("register")]
        public async Task<IActionResult> Register([FromBody] RegisterRequest request)
        {
            if (!ModelState.IsValid)
            {
                return BadRequestModelState();
            }
            var user = await _userRepository.GetUserByEmail(request.Email);
            if (user != null)
            {
                return Conflict(new ErrorResponse("Email already exists"));
            }
            
            var newUser = new User
            {
                Id = Guid.NewGuid(),
                Email = request.Email,
                Username = request.Username,
                PasswordHash = _passwordHasher.HashPassword(request.Password)
            };
            await _userRepository.Create(newUser);
            // Make the user logged in
            var response = await _authenticator.Authenticate(newUser);
            return Ok(response);
        }

        [HttpPost("login")]
        public async Task<IActionResult> Login([FromBody] LoginRequest request)
        {
            if (!ModelState.IsValid)
            {
                BadRequestModelState();
            }
            var user = await _userRepository.GetUserByEmail(request.Email);
            if (user == null)
            {
                return Unauthorized(new ErrorResponse("User not found"));
            }
            if (!_passwordHasher.CheckPassword(request.Password, user.PasswordHash))
            {
                return Unauthorized(new ErrorResponse("Invalid password"));
            }
            var response = await _authenticator.Authenticate(user);
            return Ok(response);
        }
        
        [HttpPost("refresh")]
        public async Task<IActionResult> Refresh([FromBody] RefreshTokenRequest request)
        {
            if (!ModelState.IsValid)
            {
                return BadRequestModelState();
            }
            var validatedToken = request.RefreshToken != null && _refreshTokenValidator.Validate(request.RefreshToken);
            if (!validatedToken)
            {
                return BadRequest(new ErrorResponse("Invalid refresh token"));
            }
            if (request.RefreshToken == null) return BadRequest(new ErrorResponse("Refresh token is null"));
            var refreshToken = await _refreshTokenRepository.GetByToken(request.RefreshToken);
            if (refreshToken == null)
            {
                return NotFound(new ErrorResponse("Invalid refresh token"));
            }
            var user = await _userRepository.GetUserById(refreshToken.UserId);
            if (user == null)
            {
                return NotFound(new ErrorResponse("User not found"));
            }
            await _refreshTokenRepository.Delete(refreshToken);
            var response = await _authenticator.Authenticate(user);
            return Ok(response);
        }
        
        [Authorize]
        [HttpDelete("logout")]
        public async Task<IActionResult> Logout()
        {
            var rawId = HttpContext.User.FindFirstValue("user_id");
            if (!Guid.TryParse(rawId, out Guid id))
            {
                return Unauthorized(new ErrorResponse("Invalid user id"));
            }

            await _refreshTokenRepository.DeleteAll(id);
            return Ok();
        }
        
        private IActionResult BadRequestModelState()
        {
            IEnumerable<string> errorMessages = ModelState.Values.SelectMany(v => v.Errors.Select(e => e.ErrorMessage));
            return BadRequest(new ErrorResponse(errorMessages));
        }
    }
}