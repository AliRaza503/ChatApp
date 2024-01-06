
using Auth.Models;

namespace Auth.Services.Repositories.UserRepositories;

public interface IUserRepository
{
     Task<User?> GetUserByEmail(string email);
     Task<User?> GetUserById(Guid id);
     Task<User> Create(User user);
}