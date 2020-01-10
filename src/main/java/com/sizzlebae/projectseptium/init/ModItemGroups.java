package com.sizzlebae.projectseptium.init;

import com.sizzlebae.projectseptium.ProjectSeptium;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

import java.util.function.Supplier;

public class ModItemGroups {
    public static final ItemGroup PROJECT_SEPTIUM_GROUP = new ModItemGroup(ProjectSeptium.MODID, () -> new ItemStack(ModItems.SEPITH_ITEM));

    public static class ModItemGroup extends ItemGroup {
        private final Supplier<ItemStack> iconSupplier;

        public ModItemGroup(String name, Supplier<ItemStack> iconSupplier) {
            super(name);
            this.iconSupplier = iconSupplier;

        }

        @Override
        public ItemStack createIcon() {
            return iconSupplier.get();
        }
    }
}
