namespace Auth.Models.Responses;

public class ErrorResponse
{
    public IEnumerable<string> ErrorMessages { get; set; }
    public ErrorResponse(IEnumerable<string> errorMessages)
    {
        ErrorMessages = errorMessages;
    }
    public ErrorResponse(string errorMessage)
    {
        ErrorMessages = new List<string> { errorMessage };
    }
}