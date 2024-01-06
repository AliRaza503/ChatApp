
namespace Auth.Models.Responses;

public class LoginResponse
{
    public string AccessToken { get; set; }
    public string RefreshToken { get; set; }
    
    public User User { get; set; }
}