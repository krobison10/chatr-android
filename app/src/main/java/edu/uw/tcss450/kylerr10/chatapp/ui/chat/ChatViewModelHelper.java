package edu.uw.tcss450.kylerr10.chatapp.ui.chat;

import edu.uw.tcss450.kylerr10.chatapp.model.UserInfoViewModel;

/**
 * Helper class for managing ChatViewModel related operations.
 * @author Leyla Ahmed
 */
public class ChatViewModelHelper {
    private static ChatViewModel mViewModel;

    /**
     * Sets the ChatViewModel instance to be used by the helper.
     * @param viewModel The ChatViewModel instance to set
     */
    public static void setViewModel(ChatViewModel viewModel) {
        mViewModel = viewModel;
    }

    /**
     * Removes a member from a chat room using the ChatViewModel.
     * @param chatId The ID of the chat room from which to remove the member
     * @param email  The email of the member to be removed
     */
    public static void removeMember(String chatId, String email) {
        if (mViewModel != null) {
            mViewModel.deleteChatMember(chatId, email);
        }
    }
    /**
     * Retrieves the JWT from the ChatViewModel.
     * @return The JWT string
     */
    public static String getJWT() {
        if (mViewModel != null) {
            return mViewModel.getJWT();
        }
        return null;
    }

    /**
     * Retrieves the JWT from the ChatViewModel.
     * @return The JWT string
     */
    public static String getEmail() {
        if (mViewModel != null) {
            return mViewModel.getEmail();
        }
        return null;
    }

}