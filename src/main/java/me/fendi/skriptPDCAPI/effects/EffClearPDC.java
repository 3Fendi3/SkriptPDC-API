package me.fendi.skriptPDCAPI.effects;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import me.fendi.skriptPDCAPI.utils.PDCUtils;
import org.bukkit.NamespacedKey;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public class EffClearPDC extends Effect {

    private Expression<Object> holders;
    private Expression<String> tags;
    private boolean clearAll;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        clearAll = matchedPattern == 0;

        if (clearAll) {
            holders = (Expression<Object>) expressions[0];
        } else {
            tags = (Expression<String>) expressions[0];
            holders = (Expression<Object>) expressions[1];
        }
        return true;
    }

    @Override
    protected void execute(Event event) {
        for (Object holder : holders.getArray(event)) {
            if (holder == null) continue;

            PDCUtils.editPersistentDataContainer(holder, container -> {
                if (clearAll) {
                    Set<NamespacedKey> keys = Set.copyOf(container.getKeys());
                    for (NamespacedKey key : keys) {
                        container.remove(key);
                    }
                } else {
                    for (String tagName : tags.getArray(event)) {
                        if (tagName == null) continue;
                        NamespacedKey key = PDCUtils.createKey(tagName);
                        if (key != null) {
                            container.remove(key);
                        }
                    }
                }
            }, true);
        }
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        if (clearAll) {
            return "clear all pdc of " + holders.toString(event, debug);
        } else {
            return "remove pdc tags " + tags.toString(event, debug) +
                    " from " + holders.toString(event, debug);
        }
    }
}