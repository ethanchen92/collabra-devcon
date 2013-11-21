package net.myanmarhub.collabra.provider.manager;

import android.content.ContentProviderClient;
import android.content.Context;
import android.net.Uri;
import android.os.RemoteException;

import com.appspot.myanhub_collabra.collabra.model.Conversation;

import net.myanmarhub.collabra.dao.ConversationDAO;
import net.myanmarhub.collabra.provider.CollabraKind;

import java.io.IOException;

/**
 * Tin Htoo Aung (Myanmar Hub) on 21/11/13.
 */
public class ConversationSyncManager extends BaseSyncManager<Conversation> {

    private ConversationDAO mConversationDAO;
    private static ConversationSyncManager INSTANCE;

    public static ConversationSyncManager getInstance(Context context, ContentProviderClient providerClient) {
        if (INSTANCE == null) {
            INSTANCE = new ConversationSyncManager(context, providerClient);
        }
        return INSTANCE;
    }

    public ConversationSyncManager(Context context, ContentProviderClient providerClient) {
        super(providerClient);
        mConversationDAO = new ConversationDAO(context);
    }

    @Override
    protected void processData(Conversation object) {
        mConversationDAO.save(object);
    }

    @Override
    public void delete(Long id) {
        if (id != null) {
            try {
                mProviderClient.delete(Uri.parse(CollabraKind.Conversation.CONTENT_URI + "/" + id),
                        null, null);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void deleteAll() {
        try {
            mProviderClient.delete(CollabraKind.Conversation.CONTENT_URI, null, null);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void fetchItem(Long conversationId) throws IOException {
        Conversation con = mCollabra.conversation().getById(conversationId).execute();
        if (con != null) processData(con);
    }

//    public void fetchAll() throws IOException {
//        Collection<Conversation> users = mCollabra.conversation().get().execute().getItems();
//        if (users != null) {
//            mUserDAO.deleteAll();
//            Iterator<User> iterator = users.iterator();
//            while (iterator.hasNext()) {
//                processData(iterator.next());
//            }
//        }
//    }
}
