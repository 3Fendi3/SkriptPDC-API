package me.fendi.skriptPDCAPI.expressions;

import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import me.fendi.skriptPDCAPI.utils.PDCUtils;
import org.bukkit.NamespacedKey;
import org.bukkit.event.Event;
import org.bukkit.persistence.PersistentDataContainer;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

public class ExprAllPDCTags extends SimpleExpression<String> {

    private Expression<Object> holders;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        holders = (Expression<Object>) expressions[0];
        return true;
    }

    @Override
    protected String[] get(Event event) {
        Set<String> allKeys = new HashSet<>();
        for (Object holder : holders.getArray(event)) {
            if (holder == null) continue;

            PersistentDataContainer container = PDCUtils.getContainer(holder);
            if (container == null) continue;

            Set<NamespacedKey> keys = container.getKeys();
            for (NamespacedKey key : keys) {
                allKeys.add(key.toString());
            }
        }
        return allKeys.toArray(new String[0]);
    }

    @Override
    public boolean isSingle() {
        return false;
    }

    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "all pdc tags of " + holders.toString(event, debug);
    }
}