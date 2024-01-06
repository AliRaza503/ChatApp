using Auth.Models;

namespace Auth.Services.Repositories.UserRepositories;

public class UserRepository : IUserRepository
{
    private readonly List<User> _users = new();
    public Task<User?> GetUserByEmail(string email)
    {
        return Task.FromResult(_users.FirstOrDefault(u => u.Email == email));
    }

    public Task<User?> GetUserById(Guid id)
    {
        return Task.FromResult(_users.FirstOrDefault(u => u.Id == id));
    }

    public Task<User> Create(User user)
    {
        _users.Add(user);
        return Task.FromResult(user);
    }
}