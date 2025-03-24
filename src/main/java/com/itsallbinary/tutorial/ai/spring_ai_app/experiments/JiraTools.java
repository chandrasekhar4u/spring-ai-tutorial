package com.itsallbinary.tutorial.ai.spring_ai_app.experiments;

import org.springframework.ai.tool.annotation.Tool;

public class JiraTools {

    @Tool(description = "Returns description of JIRA ticket, provided JIRA ticket number as input")
    String getJiraTicketDescription(String jiraTicketNumber) {
        System.out.println("Fetching JIRA description for jiraTicketNumber " + jiraTicketNumber);
        return "This is the description for jiraTicketNumber = " + jiraTicketNumber;
    }
}
