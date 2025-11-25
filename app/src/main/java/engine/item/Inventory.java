package engine.item;

import data.DataManager;
import data.ISerializable;
import engine.IDisplayable;
import utils.Location;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.Arrays;
import java.util.Random;

import static utils.ANSIColors.*;

/**
 * Represents an inventory that can hold weapons and recovery items.
 * The inventory has a maximum capacity for weapons and recovery items,
 * and provides methods to add, remove, and display these items.
 */
public class Inventory implements ISerializable, IDisplayable {

    public static final int maxWeaponNum = 3;
    public static final int maxRecoverNum = 5;

    ItemWeapon[] itemWeaponArray = new ItemWeapon[maxWeaponNum];
    ItemRecover[] itemRecoverArray = new ItemRecover[maxRecoverNum];

    /**
     * Constructs an Inventory and initializes it with default items if
     * configuration is not read from a file.
     */
    public Inventory() {
        if (!DataManager.READ_CONFIG_FROM_FILE) {
            generalize();
        }
    }

    /**
     * Gets the weapon at the specified index in the inventory.
     *
     * @param index the index of the weapon (1-based)
     * @return the weapon at the specified index, or null if not found
     */
    public ItemWeapon getWeapon(int index) {
        index -= 1;
        if (index < 0) {
            System.out.println(ANSI_RED + "invalid index" + ANSI_RESET);
            return null;
        }
        if (index >= maxWeaponNum) {
            System.out.println(ANSI_RED + "index out of bounds" + ANSI_RESET);
            return null;
        }
        if (itemWeaponArray[index] == null) {
            System.out.println(ANSI_RED + "this slot is no weapon" + ANSI_RESET);
            return null;
        }
        // update show the new state
        ItemWeapon ret = itemWeaponArray[index];
        showInventory();
        return ret;
    }

    /**
     * Adds a weapon to the inventory.
     *
     * @param itemWeapon the weapon to add
     * @return true if the weapon was added, false if the inventory is full
     */
    public boolean addWeapon(ItemWeapon itemWeapon) {
        for (int i = 0; i < maxWeaponNum; i++) {
            if (itemWeaponArray[i] == null) {
                itemWeaponArray[i] = itemWeapon;
                showInventory();
                return true;
            }
        }

        // not find show error
        System.out.println(ANSI_RED
                + "xxxx no rest slot for storing weapon, please remove some of the weapon use command  (rm w + index)!"
                + ANSI_RESET);
        return false;
    }

    /**
     * Removes the weapon at the specified index from the inventory.
     *
     * @param index the index of the weapon to remove (1-based)
     * @return the removed weapon, or null if not found
     */
    public ItemWeapon removeWeapon(int index) {
        index -= 1;
        if (index < 0) {
            System.out.println(ANSI_RED + "invalid index" + ANSI_RESET);
            return null;
        }
        if (index >= maxWeaponNum) {
            System.out.println(ANSI_RED + "index out of bounds" + ANSI_RESET);
            return null;
        }
        if (itemWeaponArray[index] == null) {
            System.out.println(ANSI_RED + "this slot is no weapon" + ANSI_RESET);
            return null;
        }
        // update show the new state
        ItemWeapon ret = itemWeaponArray[index];
        itemWeaponArray[index] = null;
        showInventory();
        return ret;
    }

    /**
     * Adds a recovery item to the inventory.
     *
     * @param itemRecover the recovery item to add
     * @return true if the item was added, false if the inventory is full
     */
    public boolean addRecover(ItemRecover itemRecover) {
        for (int i = 0; i < maxRecoverNum; i++) {
            if (itemRecoverArray[i] == null) {
                itemRecoverArray[i] = itemRecover;
                showInventory();
                return true;
            }
        }

        // not find show error
        System.out.println(ANSI_RED
                + "xxxx no rest slot for storing recover, please remove some of the recover use command (rm r + index)!"
                + ANSI_RESET);
        return false;
    }

    /**
     * get the recover by index
     *
     * @param index the index of recover in the slot
     * @return the recover instance in the index
     */
    public ItemRecover getRecover(int index) {
        index -= 1;

        if (index < 0) {
            System.out.println(ANSI_RED + "invalid index (negative)" + ANSI_RESET);
            return null;
        }
        if (index >= maxRecoverNum) {
            System.out.println(ANSI_RED + "index out of bounds" + ANSI_RESET);
            return null;
        }
        if (itemRecoverArray[index] == null) {
            System.out.println(ANSI_RED + "this slot is no recover" + ANSI_RESET);
            return null;
        }
        // update show the new state
        ItemRecover ret = itemRecoverArray[index];
        showInventory();
        return ret;
    }

    /**
     * Removes the recovery item at the specified index from the inventory.
     *
     * @param index the index of the recovery item to remove (1-based)
     * @return the removed recovery item, or null if not found
     */
    public ItemRecover removeRecover(int index) {
        index -= 1;

        if (index < 0) {
            System.out.println(ANSI_RED + "invalid index (negative)" + ANSI_RESET);
            return null;
        }
        if (index >= maxRecoverNum) {
            System.out.println(ANSI_RED + "index out of bounds" + ANSI_RESET);
            return null;
        }
        if (itemRecoverArray[index] == null) {
            System.out.println(ANSI_RED + "this slot is no recover" + ANSI_RESET);
            return null;
        }
        // update show the new state
        ItemRecover ret = itemRecoverArray[index];
        itemRecoverArray[index] = null;
        showInventory();
        return ret;
    }

    /**
     * Displays the current state of the inventory, showing weapons and recovery
     * items.
     */
    public void showInventory() {
        System.out.println("Inventory: " + ANSI_YELLOW + "only carry  " + maxWeaponNum + "  weapons and   "
                + maxRecoverNum + " recovers!" + " use commend (use index) to use the recover" + ANSI_RESET);

        System.out.print(ANSI_GREEN + "    --Weapons(attack): ");
        for (int i = 0; i < itemWeaponArray.length; i++) {
            ItemWeapon itemWeapon = itemWeaponArray[i];
            String weaponString;
            if (itemWeapon == null) {
                weaponString = "    ";
            } else {
                int weaponAttribute = itemWeapon.getAttributes();
                if (weaponAttribute < 10) {
                    weaponString = weaponAttribute + "   ";
                } else if (weaponAttribute < 100) {
                    weaponString = weaponAttribute + "  ";
                } else if (weaponAttribute < 1000) {
                    weaponString = weaponAttribute + " ";
                } else {
                    weaponString = String.valueOf(weaponAttribute);
                }
            }

            System.out.print("[" + (i + 1) + "]" + ":" + weaponString + "   ");
        }

        System.out.println(ANSI_RESET);

        System.out.print(ANSI_GREEN + "    --Recovers ( Hp ): ");
        for (int i = 0; i < itemRecoverArray.length; i++) {
            ItemRecover itemRecover = itemRecoverArray[i];
            String recoverString;

            if (itemRecover == null) {
                recoverString = "    ";
            } else {
                int recoverAttributes = itemRecover.getAttributes();
                if (recoverAttributes < 10) {
                    recoverString = recoverAttributes + "   ";
                } else if (recoverAttributes < 100) {
                    recoverString = recoverAttributes + "  ";
                } else if (recoverAttributes < 1000) {
                    recoverString = recoverAttributes + " ";
                } else {
                    recoverString = String.valueOf(recoverAttributes);
                }
            }
            System.out.print("[" + (i + 1) + "]" + ":" + recoverString + "   ");
        }
        System.out.println(ANSI_RESET);

    }

    /**
     * Displays the inventory.
     */
    @Override
    public void display() {
        showInventory();
    }

    /**
     * Serializes the inventory to a JSON string.
     *
     * @return a JSON representation of the inventory
     */
    @Override
    public String serialize() {
        JsonObject jsonObject = new JsonObject();
        JsonArray itemArray = new JsonArray();
        for (Item item : itemWeaponArray) {
            if (item != null) {
                itemArray.add(JsonParser.parseString(item.serialize()));
            }
        }
        for (Item item : itemRecoverArray) {
            if (item != null) {
                itemArray.add(JsonParser.parseString(item.serialize()));
            }
        }
        jsonObject.add("items", itemArray);
        return jsonObject.toString();
    }

    /**
     * Deserializes the inventory from a JSON string.
     *
     * @param data the JSON string to deserialize
     */
    @Override
    public void deserialize(String data) {
        JsonObject jsonObject = JsonParser.parseString(data).getAsJsonObject();

        // deserialize items
        JsonArray entitiesArray = jsonObject.getAsJsonArray("items");

        for (JsonElement itemElement : entitiesArray) {
            JsonObject itemObject = itemElement.getAsJsonObject();
            if (itemObject.get("type").getAsString().equals(ItemType.WEAPON.getName())) {
                ItemWeapon itemWeapon = new ItemWeapon(new Location(), 0);
                itemWeapon.deserialize(itemElement.toString());
                for (int i = 0; i < maxWeaponNum; i++) {
                    if (itemWeaponArray[i] == null) {
                        itemWeaponArray[i] = itemWeapon;
                        // after put, just break
                        break;
                    }
                }
            } else if (itemObject.get("type").getAsString().equals(ItemType.RECOVER.getName())) {
                ItemRecover itemRecover = new ItemRecover(new Location(), 0);
                itemRecover.deserialize(itemElement.toString());
                for (int i = 0; i < maxRecoverNum; i++) {
                    if (itemRecoverArray[i] == null) {
                        itemRecoverArray[i] = itemRecover;
                        // after put, just break
                        break;
                    }
                }
            }
        }
    }

    /**
     * Initializes the inventory with default items.
     */
    void generalize() {
        Random random = new Random();
        int max = 100;

        addWeapon(new ItemWeapon(new Location(), random.nextInt(max) + 1));
        addWeapon(new ItemWeapon(new Location(), random.nextInt(max) + 1));
        addWeapon(new ItemWeapon(new Location(), random.nextInt(max) + 1));

        addRecover(new ItemRecover(new Location(), random.nextInt(max) + 1));
        addRecover(new ItemRecover(new Location(), random.nextInt(max) + 1));
        addRecover(new ItemRecover(new Location(), random.nextInt(max) + 1));
    }

    /**
     * string of the inventory
     *
     * @return string of the inventory
     */
    @Override
    public String toString() {
        return "\n  weaponArray =" + Arrays.toString(itemWeaponArray) +
                "\n  recoverArray=" + Arrays.toString(itemRecoverArray);
    }
}
