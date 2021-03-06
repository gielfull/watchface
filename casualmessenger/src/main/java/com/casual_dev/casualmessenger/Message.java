package com.casual_dev.casualmessenger;

import com.casual_dev.casualmessenger.user_defined.MessageDefinition;

import java.io.Serializable;

/**
 *
 * The message hashtable and its possible values
 * Created by adamoutler on 11/10/14.
 */
public class Message extends MessageDefinition implements Serializable {


    /**
     * gets an object fom the table
     *
     * @param i item to fetch
     * @param c used for type casting
     * @return Object corresponding to Item, casted as i.type()
     */


    @SuppressWarnings("unchecked unused")  //very well checked, thanks Android Studio
    public <T> T get(ITEMS i, Class<T> c) {
        Class expectedClass = i.type();
        Object actualObject = get(i);
        try {

            if (nullTest(actualObject)) {
                actualObject = get(i.name()); //json screws with table so if the first way doesnt work, we try this
                if (nullTest(actualObject)) {
                    return null;
                }
            }
            if (!actualObject.getClass().equals(expectedClass)) {
                throw new ClassCastException("itemName " + i + " is classified as a " + i.type().getName() + " but the object passed in was a " + actualObject.getClass().getName());
            }
            return (T) expectedClass.cast(actualObject);
        } catch (ClassCastException ex) {
            ex.printStackTrace();
            return null;
        }


    }

    /**
     * put an object into the hashtable.
     *
     * @param itemName  Key for hashtable.
     * @param itemValue Value for hashtable.
     * @return value entered into hashtable
     */
    @Override
    public Object put(ITEMS itemName, Object itemValue) {
        if (values().contains(itemName)) {
            remove(itemName);
        }
        if (!itemValue.getClass().getName().equals(itemName.type().getName()) && !itemValue.equals("")) {
            throw new ClassCastException("itemName was " + itemName.type().getName() + " but the value` passed in was " + itemValue.getClass().getName());
        }
        return super.put(itemName, itemValue);
    }

    private boolean nullTest(Object o) {
        return o == null || o.hashCode() == 0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (ITEMS i : keySet()) {
            sb.append("Item:").append(i).append(" Value:").append(get(i));
        }
        return sb.toString();
    }



}
