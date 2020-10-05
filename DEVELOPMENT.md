# Development

## Testing

The project has special integration tests, which can be recognized by the tag `IntegrationTest`. These connect against a live node. To execute these tests the profile `allTests` must be active.

`mvn clean test -PallTests`
