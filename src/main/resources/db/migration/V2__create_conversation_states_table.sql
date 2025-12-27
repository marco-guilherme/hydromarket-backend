begin;

create table hydromarket.conversation_states (
	identifier serial primary key,
	conversation_identifier text,
	user_telephone_number text,
	conversation_started_at timestamp with time zone not null default now(),
	user_sent_last_message_at timestamp with time zone not null default now()
);

commit;
