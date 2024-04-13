using Microsoft.AspNetCore.Mvc;

namespace Auth.Controllers.Temp;

public class RequestsCountingController(ILogger<RequestsCountingController> logger, ICounter counter)
    : Controller
{
    private readonly ILogger<RequestsCountingController> _logger = logger;

    [HttpGet("add/{value1}")]
    public IActionResult AddValues([FromQuery] int value2, [FromRoute] int value1)
    {
        int sum = value1 + value2;
        return Ok(new { Value1 = value1, Value2 = value2, Sum = sum });
    }
    
    [HttpGet("/counter/increment")]
    public IActionResult IncrementCounter()
    {
        counter.Increment();
        return Ok(counter.Value);
    }
}