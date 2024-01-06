namespace Auth.Services.PasswordHashers;

public interface IPasswordHasher
{
    public string HashPassword(string password);
    bool CheckPassword(string requestPassword, string userPasswordHash);
}