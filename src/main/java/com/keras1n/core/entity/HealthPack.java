package com.keras1n.core.entity;

import org.joml.Vector3f;

public class HealthPack extends PickupItem {
    private final int healAmount;

    public HealthPack(MultiMaterialModel model, String modelPath, Vector3f position, Vector3f rotation,float scale, int healAmount) {
        super(model, modelPath, position, rotation, scale);
        this.healAmount = healAmount;
        this.pickUpMessage = "+ " + healAmount + " HP";
    }

    @Override
    public boolean onPickup(Player player) {
        float before = player.getHealth();

        if(player.getHealth() < 100){
            System.out.println("Picked up health pack! +" + healAmount + " HP");
            if(player.getHealth() + healAmount > 100){
                player.setHealth(100);
            }
            else{
                player.heal(healAmount);
            }
            float healed = Math.min((int)healAmount, (int)100f - before);
            int healedRounded = Math.round(healed);
            //int healedRounded = Math.round(healed); // ← округляем результат

            pickUpMessage = "+ " + healedRounded  + " HP";
            return true;
        }
        return false;
    }

    @Override
    public String getType() {
        return "HealthPack";
    }

    public int getHealAmount() {
        return healAmount;
    }
}
