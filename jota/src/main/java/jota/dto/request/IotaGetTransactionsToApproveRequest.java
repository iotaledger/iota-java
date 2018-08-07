package jota.dto.request;

import jota.IotaAPICommands;

/**
 * This class represents the core API request 'getTransactionsToApprove'.
 **/
public class IotaGetTransactionsToApproveRequest extends IotaCommandRequest {

  private Integer depth;
  private String reference;

  /**
   * Initializes a new instance of the IotaGetTransactionsToApproveRequest class.
   */
  private IotaGetTransactionsToApproveRequest(final Integer depth, final String reference) {
    super(IotaAPICommands.GET_TRANSACTIONS_TO_APPROVE);
    this.depth = depth;
    this.reference = reference;
  }

  /**
   * Create a new instance of the IotaGetTransactionsToApproveRequest class.
   */
  public static IotaGetTransactionsToApproveRequest createIotaGetTransactionsToApproveRequest(Integer depth, String reference) {
    return new IotaGetTransactionsToApproveRequest(depth, reference);
  }

  /**
   * Gets the depth.
   *
   * @return The depth.
   */
  public Integer getDepth() {
    return depth;
  }

  /**
   * /**
   * Sets the depth.
   *
   * @param depth The depth.
   */
  public void setDepth(Integer depth) {
    this.depth = depth;
  }

  /**
   * Gets the reference to use
   *
   * @return The reference point.
   */
  public String getReference() {
    return reference;
  }

  /**
   * Sets the reference.
   *
   * @param reference The reference
   */
  public void setReference(String reference) {
    this.reference = reference;
  }
}
