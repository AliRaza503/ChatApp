namespace Auth.Models;

public class AuthenticationConfiguration
{
    public string AccessTokenSecretKey { get; set; }
    public string Issuer { get; set; }
    public string Audience { get; set; }
    public int AccessTokenLifetimeInMinutes { get; set; }
    public string RefreshTokenSecretKey { get; set; }
    public int RefreshTokenLifetimeInMinutes { get; set; }
}