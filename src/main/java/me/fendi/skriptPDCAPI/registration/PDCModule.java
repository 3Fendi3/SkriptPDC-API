package me.fendi.skriptPDCAPI.registration;

import me.fendi.skriptPDCAPI.effects.EffClearPDC;
import me.fendi.skriptPDCAPI.expressions.ExprAllPDCTags;
import me.fendi.skriptPDCAPI.expressions.ExprPDC;
import org.skriptlang.skript.addon.AddonModule;
import org.skriptlang.skript.addon.SkriptAddon;
import org.skriptlang.skript.docs.Origin;
import org.skriptlang.skript.registration.DefaultSyntaxInfos;
import org.skriptlang.skript.registration.SyntaxInfo;
import org.skriptlang.skript.registration.SyntaxRegistry;

public class PDCModule implements AddonModule {

    @Override
    public String name() {
        return "PDC-Registration-Module";
    }

    @Override
    @SuppressWarnings("unchecked")
    public void load(SkriptAddon addon) {
        Origin origin = Origin.of(addon);

        addon.syntaxRegistry().register(SyntaxRegistry.EXPRESSION,
                DefaultSyntaxInfos.Expression.builder(ExprPDC.class, Object.class)
                        .addPatterns("pdc tag %string% of %objects%")
                        .origin(origin)
                        .build()
        );

        addon.syntaxRegistry().register(SyntaxRegistry.EXPRESSION,
                DefaultSyntaxInfos.Expression.builder(ExprAllPDCTags.class, String.class)
                        .addPatterns("[all] [the] pdc tag[s] of %objects%")
                        .origin(origin)
                        .build()
        );

        addon.syntaxRegistry().register(SyntaxRegistry.EFFECT,
                SyntaxInfo.builder(EffClearPDC.class)
                        .addPatterns("clear [all] pdc (of|from) %objects%",
                                "(remove|delete) pdc tag[s] %strings% (of|from) %objects%")
                        .origin(origin)
                        .build()
        );
    }
}