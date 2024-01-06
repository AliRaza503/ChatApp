using Auth.Models;

namespace Auth.Services.Repositories.RefreshTokenRepositories;

public interface IRefreshTokenRepository
{
    Task Create(RefreshToken refreshToken); 
    Task<RefreshToken?> GetByToken(string token);
    
    Task Delete(RefreshToken refreshToken);
    Task DeleteAll(Guid result);
}