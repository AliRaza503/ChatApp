
using Auth.Models;
using Microsoft.EntityFrameworkCore;

namespace Auth.Models.DB;

public class AuthenticationDbContext: DbContext
{
    public DbSet<User> Users { get; set; }
    public DbSet<RefreshToken> RefreshTokens { get; set; }
}