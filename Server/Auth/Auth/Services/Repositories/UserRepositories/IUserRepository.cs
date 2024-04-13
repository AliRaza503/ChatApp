
using Auth.Models;

namespace Auth.Services.Repositories.UserRepositories;

public interface IUserRepository
{
     Task<User?> GetUserByEmail(string email);
     Task<User?> GetUserById(Guid id);
     Task<User> Create(User user);
     List<User> GetAll();
     Task<User> Update(User user);
     Task<User> GetUserByInt(int id);
}