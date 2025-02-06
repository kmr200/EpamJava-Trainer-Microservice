Feature: Modify Training

  Scenario: Creating a new training session for trainer that is already registered
    Given trainer profile with initial trainings
    When create training request comes
    Then update trainer's trainings summary

  Scenario: Creating a new training session for trainer that has not yet registered
    Given non-existing trainer details
    When create training request comes
    Then create new trainer profile

  Scenario: Deleting training subtracts training duration from summary
    Given trainer profile with initial trainings
    When delete training request comes
    Then Subtract training duration from trainer's trainings summary

  Scenario: Deleting training from non-existing trainer returns not-found
    Given non-existing trainer details
    When delete training request comes
    Then return trainer not-found

  Scenario: Updating trainer details
    Given trainer profile with initial trainings
    When update trainer request comes
    Then update trainer details

  Scenario: Updating trainer details with insufficient details
    Given non-existing trainer details
    When update trainer request comes
    Then return trainer not-found