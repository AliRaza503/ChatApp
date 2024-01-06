using Auth.Models;
using Auth.Models.Responses;
using Auth.Services.Repositories.RefreshTokenRepositories;
using Auth.Services.TokenGenerators;

namespace Auth.Services.Authenticators;

public class Authenticator
{
    private readonly AccessTokenGenerator _accessTokenGenerator;
    private readonly RefreshTokenGenerator _refreshTokenGenerator;
    private readonly IRefreshTokenRepository _refreshTokenRepository;


    public Authenticator(AccessTokenGenerator accessTokenGenerator, RefreshTokenGenerator refreshTokenGenerator, IRefreshTokenRepository refreshTokenRepository)
    {
        _accessTokenGenerator = accessTokenGenerator;
        _refreshTokenGenerator = refreshTokenGenerator;
        _refreshTokenRepository = refreshTokenRepository;
    }

    public async Task<LoginResponse> Authenticate(User user)
    {
        var accessToken = _accessTokenGenerator.GenerateAccessToken(user);
        var newRefreshToken = _refreshTokenGenerator.GenerateRefreshToken();
        await _refreshTokenRepository.Create(new RefreshToken
        {
            Id = Guid.NewGuid(),
            Token = newRefreshToken,
            UserId = user.Id
        });
        return new LoginResponse
        {
            AccessToken = accessToken,
            RefreshToken = newRefreshToken,
            User = new User
            {
                Id = user.Id,
                Email = user.Email,
                Username = user.Username,
                PasswordHash = user.PasswordHash
            }
        };
    }
}