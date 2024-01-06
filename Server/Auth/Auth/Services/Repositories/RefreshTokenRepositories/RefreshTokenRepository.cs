using Auth.Models;
using Auth.Services.Repositories.RefreshTokenRepositories;
using Auth.Models;

namespace Auth.Services.Repositories.RefreshTokenRepositories;

public class RefreshTokenRepository: IRefreshTokenRepository
{
    private readonly List<RefreshToken> _refreshTokens = new();
    public Task Create(RefreshToken refreshToken)
    {
        _refreshTokens.Add(refreshToken);
        return Task.CompletedTask;
    }

    public Task<RefreshToken?> GetByToken(string token)
    {
        return Task.FromResult(_refreshTokens.FirstOrDefault(x => x.Token == token));
    }

    public Task Delete(RefreshToken refreshToken)
    {
        _refreshTokens.Remove(refreshToken);
        return Task.CompletedTask;
    }

    public Task DeleteAll(Guid result)
    {
        _refreshTokens.RemoveAll(x => x.UserId == result);
        return Task.CompletedTask;
    }
}