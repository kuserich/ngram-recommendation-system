package extractor;

/**
 * This enum contains all possible operation types used in {@link APIToken}.
 * 
 * An invocation can have the following types
 * 
 *  STATIC_OPERATION
 *      for static operations
 *  INSTANCE_OPERATION
 *      for normal (non-static) operations
 *  CLASS_CONSTRUCTOR
 *      for constructor operations ("new ABC()")
 */
public enum Invocation {
    STATIC_OPERATION, INSTANCE_OPERATION, CLASS_CONSTRUCTOR
}

