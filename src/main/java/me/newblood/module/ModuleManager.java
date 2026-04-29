package me.newblood.module;

import me.newblood.module.modules.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ModuleManager {
    private final List<Module> modules = new ArrayList<>();

    public ModuleManager() {
        // Combat
        modules.add(new Killaura());
        modules.add(new AutoClicker());
        modules.add(new Velocity());
        modules.add(new AutoTotem());
        modules.add(new AutoArmor());
        modules.add(new AutoGapple());
        modules.add(new AntiAim());
        modules.add(new AntiArrow());
        
        // Movement
        modules.add(new Fly());
        modules.add(new NoFall());
        modules.add(new Spider());
        modules.add(new ClickTP());
        modules.add(new NoClip());
        modules.add(new Jesus());
        modules.add(new BunnyHop());
        modules.add(new IceWalk());
        modules.add(new Parkour());
        
        // Render
        modules.add(new FullLight());
        modules.add(new ESP());
        modules.add(new XRay());
        modules.add(new Tracers());
        modules.add(new FreeCam());
        modules.add(new JumpCircle());
        modules.add(new ItemPhysics());

        // Misc
        modules.add(new Optimizer());
        modules.add(new AttackAnimation());
        modules.add(new SpeedMine());
        modules.add(new AutoRespawn());
    }

    public List<Module> getModules() {
        return modules;
    }

    public List<Module> getModulesByCategory(Module.Category category) {
        return modules.stream().filter(m -> m.getCategory() == category).collect(Collectors.toList());
    }

    public Module getModuleByName(String name) {
        return modules.stream().filter(m -> m.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }
}
