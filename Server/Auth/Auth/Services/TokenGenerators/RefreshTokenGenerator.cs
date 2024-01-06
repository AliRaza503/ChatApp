using Auth.Models;

namespace Auth.Services.TokenGenerators;

public class RefreshTokenGenerator
{
    private readonly AuthenticationConfiguration _configuration;
    private readonly TokenGenerator _tokenGenerator;

    public RefreshTokenGenerator(AuthenticationConfiguration configuration, TokenGenerator tokenGenerator)
    {
        _configuration = configuration;
        _tokenGenerator = tokenGenerator;
    }

    public string GenerateRefreshToken()
    {
        return _tokenGenerator.GenerateToken(
            issuer: _configuration.Issuer,
            audience: _configuration.Audience,
            expiryInMinutes: _configuration.RefreshTokenLifetimeInMinutes,
            key: _configuration.RefreshTokenSecretKey
            );
    }
}