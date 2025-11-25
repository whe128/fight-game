package data;

/**
 * serialize and deserialize use gson
 */
public interface ISerializable {

    /**
     * serialize object to json string
     *
     * @return json string
     */
    String serialize();

    /**
     * deserialize json string to object
     */
    void deserialize(String data);
}
