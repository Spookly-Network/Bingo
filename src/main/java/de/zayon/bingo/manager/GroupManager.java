package de.zayon.bingo.manager;

import de.dytanic.cloudnet.driver.permission.IPermissionGroup;
import de.dytanic.cloudnet.driver.permission.IPermissionUser;
import de.dytanic.cloudnet.ext.cloudperms.CloudPermissionsPermissionManagement;
import org.bukkit.entity.Player;

public class GroupManager {
    public String getGroupName(Player player) {
        IPermissionUser permissionUser = CloudPermissionsPermissionManagement.getInstance().getUser(player.getUniqueId());
        IPermissionGroup permissionGroup = CloudPermissionsPermissionManagement.getInstance().getHighestPermissionGroup(permissionUser);
        return permissionGroup.getName();
    }

    public String getGroupColor(Player player) {
        IPermissionUser permissionUser = CloudPermissionsPermissionManagement.getInstance().getUser(player.getUniqueId());
        IPermissionGroup permissionGroup = CloudPermissionsPermissionManagement.getInstance().getHighestPermissionGroup(permissionUser);
        return permissionGroup.getDisplay();
    }

    public String getGroupWriteColor(Player player) {
        IPermissionUser permissionUser = CloudPermissionsPermissionManagement.getInstance().getUser(player.getUniqueId());
        IPermissionGroup permissionGroup = CloudPermissionsPermissionManagement.getInstance().getHighestPermissionGroup(permissionUser);
        return permissionGroup.getSuffix();
    }

    public String getColoredGroupName(Player player) {
        return getGroupColor(player) + getGroupName(player);
    }
}
