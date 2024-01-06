namespace Auth.Services.PasswordHashers;

public class BCryptPasswordHasher : IPasswordHasher
{
    public string HashPassword(string password)
    {
        return BCrypt.Net.BCrypt.HashPassword(password); 
    }

    public bool CheckPassword(string requestPassword, string userPasswordHash)
    {
        return BCrypt.Net.BCrypt.Verify(requestPassword, userPasswordHash);
    }
}