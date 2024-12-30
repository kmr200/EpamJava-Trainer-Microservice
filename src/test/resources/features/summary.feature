Feature: Training Summary

  Scenario: Valid request to get summary
    Given trainer profile
    When training summary requested
    Then return summary

  Scenario: Valid request to get non-empty summary
    Given trainer profile with trainings registered
    When training summary requested
    Then return non-empty summary

  Scenario: Invalid request to get summary
    Given non-existing trainer profile
    When training summary requested
    Then return not-found