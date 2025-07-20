package com.davidelatina.bankingdemo.exception;

/**
 * Custom exception indicating that a requested user could not be found.
 * This exception is typically thrown by service-layer components (e.g.,
 * {@code CustomerService})
 * when an operation attempts to retrieve a user that does not exist based on
 * provided criteria
 * (e.g., username, ID).
 *
 * <p>
 * This is a checked exception, meaning methods that might throw it must
 * either declare it in their {@code throws} clause or handle it.
 * </p>
 *
 * @see com.davidelatina.bankingdemo.model.service.CustomerService
 */
public class UserNotFoundException extends Exception {

  /**
   * Constructs a new {@code UserNotFoundException} with {@code null} as its
   * detail message. The cause is not initialized, and may subsequently be
   * initialized by a call to {@link Throwable#initCause(Throwable)}.
   */
  public UserNotFoundException() {
    super();
  }

  /**
   * Constructs a new {@code UserNotFoundException} with the specified detail
   * message.
   * The cause is not initialized, and may subsequently be initialized by a
   * call to {@link Throwable#initCause(Throwable)}.
   *
   * @param message The detail message (which is saved for later retrieval by the
   *                {@link Throwable#getMessage()} method).
   */
  public UserNotFoundException(String message) {
    super(message);
  }

  /**
   * Constructs a new {@code UserNotFoundException} with the specified detail
   * message and
   * cause.
   * <p>
   * Note that the detail message associated with {@code cause} is
   * <i>not</i> automatically incorporated in this exception's detail
   * message.
   * </p>
   *
   * @param message The detail message (which is saved for later retrieval by the
   *                {@link Throwable#getMessage()} method).
   * @param cause   The cause (which is saved for later retrieval by the
   *                {@link Throwable#getCause()} method). (A {@code null} value is
   *                permitted, and indicates that the cause is nonexistent or
   *                unknown.)
   */
  public UserNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }
}