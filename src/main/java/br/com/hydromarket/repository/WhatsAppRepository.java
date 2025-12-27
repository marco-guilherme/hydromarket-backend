package br.com.hydromarket.repository;

import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import br.com.hydromarket.jooq.generated.tables.pojos.ConversationStates;

import java.time.OffsetDateTime;

import static br.com.hydromarket.jooq.generated.tables.ConversationStates.CONVERSATION_STATES;

@Repository
public class WhatsAppRepository {
    private final DSLContext context;

    public WhatsAppRepository(DSLContext context) {
        this.context = context;
    }

    public void insertConversation(ConversationStates conversationState) {
        this.context.insertInto(CONVERSATION_STATES)
                .set(context.newRecord(CONVERSATION_STATES, conversationState))
                .execute();
    }

    public ConversationStates findConversationStateByConversationIdentifier(String conversationIdentifier) {
        return this.context.selectFrom(CONVERSATION_STATES)
                .where(CONVERSATION_STATES.CONVERSATION_IDENTIFIER.equal(conversationIdentifier))
                .fetchOneInto(ConversationStates.class);
    }

    public void updateUserSentLastMessageTimestampByConversationIdentifier(String conversationIdentifier) {
        this.context.update(CONVERSATION_STATES)
                .set(CONVERSATION_STATES.USER_SENT_LAST_MESSAGE_AT, OffsetDateTime.now())
                .where(CONVERSATION_STATES.CONVERSATION_IDENTIFIER.equal(conversationIdentifier))
                .execute();
    }
}
