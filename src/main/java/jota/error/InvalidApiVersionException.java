package jota.error;

    /**
     * This exception occurs when invalid api version is provided.
     *
     * @author pinpong
     */
    public class InvalidApiVersionException extends BaseException {

        /**
         * Initializes a new instance of the InvalidApiVersionException.
         */
        public InvalidApiVersionException() {
            super("Invalid api version");
        }
}
