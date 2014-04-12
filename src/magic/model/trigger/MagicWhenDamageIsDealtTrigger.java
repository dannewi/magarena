package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicCard;
import magic.model.MagicDamage;
import magic.model.MagicPermanent;
import magic.model.MagicPayedCost;
import magic.model.MagicCardDefinition;
import magic.model.stack.MagicCardOnStack;
import magic.model.choice.MagicMayChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSourceEvent;
import magic.model.action.MagicPutItemOnStackAction;
import magic.model.action.MagicChangePoisonAction;
import magic.model.action.MagicCastFreeCopyAction;

public abstract class MagicWhenDamageIsDealtTrigger extends MagicTrigger<MagicDamage> {
    public MagicWhenDamageIsDealtTrigger(final int priority) {
        super(priority);
    }

    public MagicWhenDamageIsDealtTrigger() {}
    
    public boolean accept(final MagicPermanent permanent, final MagicDamage damage) {
        return damage.getDealtAmount() > 0;
    }

    public MagicTriggerType getType() {
        return MagicTriggerType.WhenDamageIsDealt;
    }

    public static MagicWhenDamageIsDealtTrigger DamageToPlayer(final MagicSourceEvent sourceEvent) {
        return new MagicWhenDamageIsDealtTrigger() {
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
                return (damage.isSource(permanent) && damage.isTargetPlayer()) ?
                    sourceEvent.getEvent(permanent) :MagicEvent.NONE;
            }
        };
    }
    
    public static MagicWhenDamageIsDealtTrigger DamageToOpponent(final MagicSourceEvent sourceEvent) {
        return new MagicWhenDamageIsDealtTrigger() {
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
                return (damage.isSource(permanent) && permanent.isOpponent(damage.getTarget())) ?
                    sourceEvent.getEvent(permanent) :MagicEvent.NONE;
            }
        };
    }
    
    public static MagicWhenDamageIsDealtTrigger CombatDamageToPlayer(final MagicSourceEvent sourceEvent) {
        return new MagicWhenDamageIsDealtTrigger() {
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
                return (damage.isSource(permanent) && damage.isCombat() && damage.isTargetPlayer()) ?
                    sourceEvent.getEvent(permanent) :MagicEvent.NONE;
            }
        };
    }

    public static MagicWhenDamageIsDealtTrigger Cipher(final MagicCardDefinition cardDef) {
        return new MagicWhenDamageIsDealtTrigger() {
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
                return (damage.isSource(permanent) && damage.isCombat() && damage.isTargetPlayer()) ?
                    new MagicEvent(
                        permanent,
                        new MagicMayChoice(),
                        this,
                        "PN may$ cast " + cardDef + " without paying its mana cost"
                    ):
                    MagicEvent.NONE;
            }
            @Override
            public void executeEvent(final MagicGame game, final MagicEvent event) {
                if (event.isYes()) {
                    game.doAction(new MagicCastFreeCopyAction(event.getPlayer(), cardDef));
                }
            }
        };
    }

    public static MagicWhenDamageIsDealtTrigger Poisonous(final int n) {
        return new MagicWhenDamageIsDealtTrigger() {
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
                return (damage.isSource(permanent) && damage.isCombat() && damage.isTargetPlayer()) ?
                    new MagicEvent(
                        permanent,
                        damage.getTarget(),
                        this,
                        n == 1 ?
                            "RN gets a poison counter." :
                            "RN gets " + n + " poison counters."
                    ):
                    MagicEvent.NONE;
            }

            @Override
            public void executeEvent(final MagicGame game, final MagicEvent event) {
                game.doAction(new MagicChangePoisonAction(event.getRefPlayer(),n));
            }
        };
    }
}
