package org.joelson.turf.application.view;

import org.joelson.turf.application.model.ApplicationData;
import org.joelson.turf.application.model.UserData;

import javax.swing.Action;
import java.awt.Container;

public final class UniqueRoundZoneGraphActionCreator {

    private UniqueRoundZoneGraphActionCreator() throws InstantiationException {
        throw new InstantiationException("Should not be instantiated");
    }

    public static Action create(ApplicationUI applicationUI) {
        Action action = new ActionBuilder(actionEvent -> showZoneRoundUniqueGraph(applicationUI))
                .withName("Show Unique Round Zone Graph").build();
        action.setEnabled(false);
        ActionUtil.addEnabledPropertyChangeListener(applicationUI.getApplicationData(), action,
                ApplicationData.HAS_DATABASE, true);
        return action;
    }

    private static void showZoneRoundUniqueGraph(ApplicationUI applicationUI) {
        ApplicationData applicationData = applicationUI.getApplicationData();
        UserData selectedUser = UserSelectionUtil.getSelectedUser(applicationData);
        UniqueRoundZoneGraphModel uniqueRoundZoneGraphModel = new UniqueRoundZoneGraphModel(applicationData.getVisits(),
                selectedUser);
        Container chartContainer = uniqueRoundZoneGraphModel.getChart();
        applicationUI.setPane(UserSelectionUtil.createContainer(applicationData, selectedUser,
                uniqueRoundZoneGraphModel::updateSelectedUser, chartContainer));
        applicationUI.setApplicationDataStatus();
    }
}
