package com.nexters.jjanji.domain.challenge.domain.repository;

import com.nexters.jjanji.domain.challenge.domain.QChallenge;
import com.nexters.jjanji.domain.challenge.domain.QParticipation;
import com.nexters.jjanji.domain.challenge.dto.response.ParticipationResponseDto;
import com.nexters.jjanji.domain.challenge.dto.response.QParticipationResponseDto;
import com.nexters.jjanji.domain.member.domain.QMember;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ParticipationDao {

    private final JPAQueryFactory queryFactory;
    private static final QChallenge challenge = QChallenge.challenge;
    private static final QParticipation participation = QParticipation.participation;
    private static final QMember member = QMember.member;

    public ParticipationDao(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    public List<ParticipationResponseDto> getParticipateList(Long memberId, Long cursor, Long size) {
        return queryFactory
                .select(new QParticipationResponseDto(
                        participation.challenge.id,
                        participation.challenge.startAt,
                        participation.challenge.endAt,
                        participation.challenge.month,
                        participation.challenge.week,
                        participation.challenge.state,
                        participation.id,
                        participation.goalAmount,
                        participation.currentAmount
                ))
                .from(participation)
                .join(participation.challenge, challenge)
                .join(participation.member, member)
                .where(
                        member.id.eq(memberId),
                        cursorPagination(cursor)
                )
                .orderBy(participation.id.desc())
                .limit(size)
                .fetch();
    }

    private Predicate cursorPagination(Long lastPostId) {
        if (lastPostId == null) {
            return null;
        }
        return participation.id.lt(lastPostId);
    }

}
