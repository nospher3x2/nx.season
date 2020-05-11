package com.redenexus.season.util.serializer;

import com.redenexus.season.util.reflection.Reflection;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * @author oNospher
 **/
public class ItemSerializer {

    public static String toBase64(org.bukkit.inventory.ItemStack itemStack) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutput = new DataOutputStream(outputStream);

        try {
            Object nbtTagListItems = Reflection.getClass_NBTTagList().newInstance();
            Object nbtTagCompoundItem = Reflection.getClass_NBTTagCompound().newInstance();

            Object nms = Reflection.getMethod_asNMSCopy().invoke(null, itemStack);

            Reflection.getMethod_SaveItem().invoke(nms, nbtTagCompoundItem);

            Reflection.getMethod_Add().invoke(nbtTagListItems, nbtTagCompoundItem);

            Reflection.getMethod_Save().invoke(null, nbtTagCompoundItem, dataOutput);
        } catch (Throwable ex) {
            ex.printStackTrace();
        }

        return new BigInteger(1, outputStream.toByteArray()).toString(32);
    }

    public static ItemStack fromBase64(String data) {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new BigInteger(data, 32).toByteArray());
        Object nbtTagCompoundRoot;
        Object nmsItem = null;
        Object toReturn = null;

        try {
            nbtTagCompoundRoot = Reflection.getMethod_A().invoke(null, new DataInputStream(inputStream));
            if (nbtTagCompoundRoot != null) {
                nmsItem = Reflection.getMethod_CreateStack().invoke(null, nbtTagCompoundRoot);
            }

            toReturn = Reflection.getMethod_AsBukkitCopy().invoke(null, nmsItem);
        } catch (Throwable ex) {
            ex.printStackTrace();
        }

        return (ItemStack) toReturn;
    }

    public static String toBase64List(List<ItemStack> items) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        BukkitObjectOutputStream dataOutput;
        try {
            dataOutput = new BukkitObjectOutputStream(outputStream);
            // Content Size
            // Contents
            dataOutput.writeInt(items.size());
            int index = 0;
            for (ItemStack is : items) {
                if (is != null && is.getType() != Material.AIR) {
                    dataOutput.writeObject(toBase64(is));
                } else {
                    dataOutput.writeObject(null);
                }
                dataOutput.writeInt(index);
                index++;
            }
            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        }
        catch (Exception e) {
            throw new IllegalStateException("Unable to save item stacks.", e);
        }
    }

    public static List<ItemStack> fromBase64List(String items) {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(items));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            int size = dataInput.readInt();
            List<ItemStack> list = new ArrayList<>();
            // Read the serialized inventory
            for (int i = 0; i < size; i++) {
                Object utf = dataInput.readObject();
                int slot = dataInput.readInt();
                if (utf != null) {
                    list.add(fromBase64((String) utf));
                }
            }
            dataInput.close();
            return list;
        }
        catch (Exception e) {
            throw new IllegalStateException("Unable to load item stacks.", e);
        }
    }
}