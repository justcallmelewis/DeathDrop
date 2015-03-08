package net.alextwelshie.deathdrop.runnables;

import java.util.concurrent.Callable;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

@SuppressWarnings("rawtypes")
public class EffectAddInRunnable implements Callable {

    private final Player player;
    private final PotionEffectType type;
    private final int duration, strength;
    
    public EffectAddInRunnable(Player player, PotionEffectType type, int duration, int strength) {
        this.player = player;
        this.type = type;
        this.duration = duration;
        this.strength = strength;
    }
    
    @Override
    public Object call() {
        player.addPotionEffect(new PotionEffect(type, duration, strength));
        return null;
    }

}
