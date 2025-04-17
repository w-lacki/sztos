package me.wiktorlacki.stos.contest

class ContestNotFoundException : Exception("Contest not found.")

class ContestAlreadyContainsUserException : Exception("Contest already contains this user.")

class ContestDoesNotContainUserException : Exception("Contest does not contain this user.")

class ContestNotAllowedException : Exception("You are not allowed to this contest.")

class ContestNotAllowedToModifyException : Exception("You are not allowed to modify this contest.")

class ContestDeadlinePassed : Exception("Contest deadline passed.")