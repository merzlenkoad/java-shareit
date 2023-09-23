package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.util.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByBookerIdOrderByStartDesc(Long bookerId, Pageable pageRequest);

    List<Booking> findByBookerIdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(
            Long bookerId, LocalDateTime start, LocalDateTime end, Pageable pageRequest);

    List<Booking> findByBookerIdAndEndIsBeforeOrderByStartDesc(Long bookerId, LocalDateTime end, Pageable pageRequest);

    List<Booking> findByBookerIdAndStartIsAfterOrderByStartDesc(
            Long bookerId, LocalDateTime start, Pageable pageRequest);

    List<Booking> findByBookerIdAndStatusOrderByStartDesc(Long bookerId, BookingStatus status, Pageable pageRequest);

    List<Booking> findByItemOwnerIdOrderByStartDesc(Long ownerId, Pageable pageRequest);

    List<Booking> findByItemOwnerIdAndStartIsAfterOrderByStartDesc(
            Long ownerId, LocalDateTime start, Pageable pageRequest);

    List<Booking> findByItemOwnerIdAndStatusOrderByStartDesc(
            Long ownerId, BookingStatus bookingStatus, Pageable pageRequest);

    List<Booking> findByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(
            Long ownerId, LocalDateTime time, LocalDateTime secondTime, Pageable pageRequest);

    List<Booking> findByItemOwnerIdAndEndBeforeOrderByStartDesc(Long ownerId, LocalDateTime time, Pageable pageRequest);

    List<Booking> findByItemId(Long itemId);

    List<Booking> findByItemOwnerId(Long ownerId);

    List<Booking> findByBookerIdAndItemIdAndEndBefore(Long bookerId, Long itemId, LocalDateTime time);

}
