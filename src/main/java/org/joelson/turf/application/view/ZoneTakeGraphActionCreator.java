package org.joelson.turf.application.view;

import org.joelson.turf.application.model.ApplicationData;
import org.joelson.turf.application.model.UserData;

import javax.swing.Action;
import java.awt.Container;

final class ZoneTakeGraphActionCreator {

    private ZoneTakeGraphActionCreator() throws InstantiationException {
        throw new InstantiationException("Should not be instantiated");
    }

    public static Action create(ApplicationUI applicationUI) {
        Action action = new ActionBuilder(actionEvent -> showZoneTakeGraph(applicationUI))
                .withName("Show Zone Take Graph").build();
        action.setEnabled(false);
        ActionUtil.addEnabledPropertyChangeListener(applicationUI.getApplicationData(), action,
                ApplicationData.HAS_DATABASE, true);
        return action;
    }

    private static void showZoneTakeGraph(ApplicationUI applicationUI) {
        ApplicationData applicationData = applicationUI.getApplicationData();
        UserData selectedUser = UserSelectionUtil.getSelectedUser(applicationData);
        ZoneTakeGraphModel zoneTakeGraphModel = new ZoneTakeGraphModel(applicationData.getVisits(), selectedUser);
        Container chartContainer = zoneTakeGraphModel.getChart();
        applicationUI.setPane(
                UserSelectionUtil.createContainer(applicationData, selectedUser, zoneTakeGraphModel::updateSelectedUser,
                        chartContainer));
        applicationUI.setApplicationDataStatus();
    }
}
