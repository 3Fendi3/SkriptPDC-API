package me.fendi.skriptPDCAPI.conditions;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import me.fendi.skriptPDCAPI.utils.PDCUtils;
import org.bukkit.event.Event;
import org.bukkit.persistence.PersistentDataContainer;
import org.jetbrains.annotations.Nullable;

public class CondPDCIsEmpty extends Condition {

    static {
        Skript.registerCondition(CondPDCIsEmpty.class,
                "[the] pdc (of|from) %itemtypes/blocks/entities% is empty",
                "[the] pdc (of|from) %itemtypes/blocks/entities% is(n't| not) empty");
    }

    private Expression<Object> holders;

    @Override
    @SuppressWarnings("unchecked")
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        holders = (Expression<Object>) expressions[0];
        setNegated(matchedPattern == 1);
        return true;
    }

    @Override
    public boolean check(Event event) {
        for (Object holder : holders.getArray(event)) {
            if (holder == null) continue;

            PersistentDataContainer container = PDCUtils.getContainer(holder);
            boolean isEmpty = (container == null || container.getKeys().isEmpty());

            if (isEmpty == isNegated()) {
                return false;
            }
        }

        return true;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "pdc of " + holders.toString(event, debug) +
                (isNegated() ? " is not empty" : " is empty");
    }
}