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

    public List<User> GetAll()
    {
        return _users;
    }

    public Task<User> Update(User user)
    {
        var index = _users.FindIndex(u => u.Id == user.Id);
        _users[index] = user;
        return Task.FromResult(user);
    }

    public Task<User> GetUserByInt(int index)
    {
        if (_users.Count > index)
        {
            return Task.FromResult(_users[index]);
        }
        
        return Task.FromResult(_users[0]);
        
    }
}