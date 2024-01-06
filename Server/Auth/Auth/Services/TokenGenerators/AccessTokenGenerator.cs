using System.Security.Claims;
using Auth.Models;
using Auth.Models;

namespace Auth.Services.TokenGenerators;

public class AccessTokenGenerator
{
    private readonly AuthenticationConfiguration _configuration;
    private readonly TokenGenerator _tokenGenerator;

    public AccessTokenGenerator(AuthenticationConfiguration configuration, TokenGenerator tokenGenerator)
    {
        _configuration = configuration;
        _tokenGenerator = tokenGenerator;
    }

    public string GenerateAccessToken(User user)
    {
        var claims = new[]
        {
            new Claim("user_id", user.Id.ToString()),
            new Claim(ClaimTypes.Email, user.Email),
            new Claim(ClaimTypes.Name, user.Username)
        };
        return _tokenGenerator.GenerateToken(
            issuer: _configuration.Issuer,
            audience: _configuration.Audience,
            expiryInMinutes: _configuration.AccessTokenLifetimeInMinutes,
            key: _configuration.AccessTokenSecretKey,
            claims: claims);
    }
}