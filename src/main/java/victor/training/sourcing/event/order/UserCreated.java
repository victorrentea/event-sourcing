package victor.training.sourcing.event.order;

public record UserCreated(
    String email,
    String firstName,
    String lastName,
    String password
) {
}
