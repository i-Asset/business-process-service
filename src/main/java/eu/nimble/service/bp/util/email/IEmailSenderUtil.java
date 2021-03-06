package eu.nimble.service.bp.util.email;

import eu.nimble.service.bp.model.hyperjaxb.ProcessInstanceGroupDAO;

import java.util.Date;

public interface IEmailSenderUtil {
    void notifyTrustScoreUpdate(String partyID, String federationID, String bearerToken);

    void sendCollaborationStatusEmail(String bearerToken, String originalBearerToken, String clientFederationId,ProcessInstanceGroupDAO groupDAO);

    void sendNewDeliveryDateEmail(String bearerToken, Date newDeliveryDate, String buyerPartyId, String buyerPartyFederationId, String sellerFederationId, String processInstanceId);

    void sendActionPendingEmail(String bearerToken, String originalBearerToken, String clientFederationId,String documentId);

    void notifyPartyOnPendingCollaboration(String[] toEmail, String initiatingPersonName, String productName, String initiatingPartyName, String url, String subject, String respondingPartyName);

    void notifyPartyOnCollaboration(String[] toEmail, String initiatingPersonName, String productName, String initiatingPartyName, String url, String subject, String respondingPartyName);

    void notifyPartyOnNewDeliveryDate(String toEmail,String productName, String respondingPartyName, String expectedDeliveryDate, String url);
}
