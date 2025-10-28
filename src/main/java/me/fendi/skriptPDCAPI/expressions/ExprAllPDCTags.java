package me.fendi.skriptPDCAPI.expressions;

import ch.njol.skript.expressions.base.PropertyExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import me.fendi.skriptPDCAPI.utils.PDCUtils;
import org.bukkit.NamespacedKey;
import org.bukkit.event.Event;
import org.bukkit.persistence.PersistentDataContainer;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

public class ExprAllPDCTags extends PropertyExpression<Object, String> {

    static {
        register(ExprAllPDCTags.class, String.class, "[all] [the] pdc tag[s]", "itemtypes/blocks/entities");
    }

    @Override
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        setExpr(expressions[0]);
        return true;
    }

    @Override
    protected String[] get(Event event, Object[] source) {
        Set<String> allKeys = new HashSet<>();

        for (Object holder : source) {
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
    public Class<? extends String> getReturnType() {
        return String.class;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "all pdc tags of " + getExpr().toString(event, debug);
    }
}