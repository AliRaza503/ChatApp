using System.ComponentModel.DataAnnotations;

namespace Auth.Models.Requests;

public class RefreshTokenRequest
{
    [Required]
    public string? RefreshToken { get; set; }
}