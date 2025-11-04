package me.fendi.skriptPDCAPI.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.registrations.Classes;
import ch.njol.util.Kleenean;
import me.fendi.skriptPDCAPI.utils.PDCUtils;
import org.bukkit.NamespacedKey;
import org.bukkit.event.Event;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static ch.njol.util.StringUtils.hexStringToByteArray;

public class ExprPDC extends SimpleExpression<Object> {

    static {
        Skript.registerExpression(ExprPDC.class, Object.class, ExpressionType.COMBINED,
                "pdc tag %string% of %objects%");
    }

    private Expression<String> tag;
    private Expression<Object> holders;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        tag = (Expression<String>) expressions[0];
        holders = (Expression<Object>) expressions[1];
        return true;
    }

    @Override
    protected Object[] get(Event event) {
        String tagName = tag.getSingle(event);
        if (tagName == null) return new Object[0];

        NamespacedKey key = PDCUtils.createKey(tagName);
        if (key == null) return new Object[0];

        List<Object> values = new ArrayList<>();
        for (Object holder : holders.getArray(event)) {
            if (holder == null) continue;

            PersistentDataContainer container = PDCUtils.getContainer(holder);
            if (container != null) {
                Object value = getPDCValue(container, key);
                if (value != null) {
                    values.add(value);
                }
            }
        }
        return values.toArray(new Object[0]);
    }

    @Override
    public boolean isSingle() {
        return holders.isSingle();
    }

    @Override
    public Class<?> getReturnType() {
        return Object.class;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "pdc tag " + tag.toString(event, debug) + " of " + holders.toString(event, debug);
    }

    @Override
    public Class<?> @Nullable [] acceptChange(Changer.ChangeMode mode) {
        return switch (mode) {
            case SET, DELETE, ADD, REMOVE -> new Class<?>[]{Object.class};
            default -> null;
        };
    }

    @Override
    public void change(Event event, Object @Nullable [] delta, Changer.ChangeMode mode) {
        String tagName = tag.getSingle(event);
        if (tagName == null) return;

        NamespacedKey key = PDCUtils.createKey(tagName);
        if (key == null) return;

        for (Object holder : holders.getArray(event)) {
            if (holder == null) continue;

            PDCUtils.editPersistentDataContainer(holder, container -> {
                switch (mode) {
                    case DELETE -> container.remove(key);
                    case SET -> {
                        if (delta != null && delta.length > 0) {
                            setPDCValue(container, key, delta[0]);
                        }
                    }
                    case ADD -> {
                        if (delta != null && delta.length > 0) {
                            Object current = getPDCValue(container, key);
                            if (current instanceof Number && delta[0] instanceof Number) {
                                double result = ((Number) current).doubleValue() +
                                        ((Number) delta[0]).doubleValue();
                                setPDCValue(container, key, result);
                            }
                        }
                    }
                    case REMOVE -> {
                        if (delta != null && delta.length > 0) {
                            Object current = getPDCValue(container, key);
                            if (current instanceof Number && delta[0] instanceof Number) {
                                double result = ((Number) current).doubleValue() -
                                        ((Number) delta[0]).doubleValue();
                                setPDCValue(container, key, result);
                            }
                        }
                    }
                }
            }, true);
        }
    }

    private @Nullable Object getPDCValue(PersistentDataContainer container, NamespacedKey key) {
        if (container.has(key, PersistentDataType.STRING)) {
            String stringValue = container.get(key, PersistentDataType.STRING);
            if (stringValue != null) {
                Object deserialized = deserialize(stringValue);
                return deserialized != null ? deserialized : stringValue;
            }
        }
        if (container.has(key, PersistentDataType.INTEGER)) {
            return container.get(key, PersistentDataType.INTEGER);
        }
        if (container.has(key, PersistentDataType.DOUBLE)) {
            return container.get(key, PersistentDataType.DOUBLE);
        }
        if (container.has(key, PersistentDataType.LONG)) {
            return container.get(key, PersistentDataType.LONG);
        }
        if (container.has(key, PersistentDataType.FLOAT)) {
            return container.get(key, PersistentDataType.FLOAT);
        }
        if (container.has(key, PersistentDataType.BYTE)) {
            return container.get(key, PersistentDataType.BYTE);
        }
        return null;
    }

    private void setPDCValue(PersistentDataContainer container, NamespacedKey key, Object value) {
        if (value instanceof Integer) {
            container.set(key, PersistentDataType.INTEGER, (Integer) value);
        } else if (value instanceof Double) {
            container.set(key, PersistentDataType.DOUBLE, (Double) value);
        } else if (value instanceof Long) {
            container.set(key, PersistentDataType.LONG, (Long) value);
        } else if (value instanceof Float) {
            container.set(key, PersistentDataType.FLOAT, (Float) value);
        } else if (value instanceof Byte) {
            container.set(key, PersistentDataType.BYTE, (Byte) value);
        } else if (value instanceof String) {
            container.set(key, PersistentDataType.STRING, (String) value);
        } else {
            container.set(key, PersistentDataType.STRING, serialize(value));
        }
    }

    private @NotNull String serialize(Object object) {
        var value = Classes.serialize(object);
        if (value == null) return "";
        return value.type + ":" + bytesToHex(value.data);
    }

    private @Nullable Object deserialize(String input) {
        if (input == null) return null;
        var values = input.split(":", 2);
        if (values.length < 2) return null;
        var type = values[0];
        var data = values[1];
        try {
            return Classes.deserialize(type, hexStringToByteArray(data));
        } catch (Exception e) {
            return null;
        }
    }

    private static final byte[] HEX_ARRAY = "0123456789ABCDEF".getBytes(StandardCharsets.US_ASCII);

    private static String bytesToHex(byte[] bytes) {
        byte[] hexChars = new byte[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars, StandardCharsets.UTF_8);
    }
}