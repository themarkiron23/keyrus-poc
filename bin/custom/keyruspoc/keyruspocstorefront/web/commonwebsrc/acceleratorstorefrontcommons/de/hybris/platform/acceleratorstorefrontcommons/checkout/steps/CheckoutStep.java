/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorstorefrontcommons.checkout.steps;


import de.hybris.platform.acceleratorstorefrontcommons.checkout.steps.validation.CheckoutStepValidator;
import de.hybris.platform.acceleratorstorefrontcommons.checkout.steps.validation.ValidationResults;

import java.util.Map;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


public class CheckoutStep implements StepTransition
{
	public static final String PREVIOUS = "previous";
	public static final String CURRENT = "current";
	public static final String NEXT = "next";
	private Map<String, String> transitions;
	private CheckoutStepValidator checkoutStepValidator;
	private CheckoutGroup checkoutGroup;
	private String progressBarId;

	@Override
	public String go(final String transition)
	{
		if (getTransitions().containsKey(transition))
		{
			return getTransitions().get(transition);
		}
		return null;
	}

	@Override
	public String onValidation(final ValidationResults validationResult)
	{
		if (getCheckoutGroup().getValidationResultsMap().containsKey(validationResult.name()))
		{
			return getCheckoutGroup().getValidationResultsMap().get(validationResult.name());
		}
		return null;
	}

	public ValidationResults validate(final RedirectAttributes redirectAttributes)
	{
		return (getCheckoutStepValidator() != null) ? getCheckoutStepValidator().validateOnEnter(redirectAttributes)
				: ValidationResults.SUCCESS;
	}

	public boolean checkIfValidationErrors(final ValidationResults validationResult)
	{
		return !validationResult.equals(ValidationResults.SUCCESS);
	}

	public boolean isEnabled()
	{
		return true;
	}

	public String previousStep()
	{
		return go(PREVIOUS);
	}

	public String currentStep()
	{
		return go(CURRENT);
	}

	public String nextStep()
	{
		if (getCheckoutStepValidator() != null)
		{
			final ValidationResults validationResult = getCheckoutStepValidator().validateOnExit();
			if (validationResult != null && !validationResult.equals(ValidationResults.SUCCESS))
			{
				return onValidation(validationResult);
			}
		}
		return go(NEXT);
	}

	public Map<String, String> getTransitions()
	{
		return transitions;
	}

	@Required
	public void setTransitions(final Map<String, String> transitions)
	{
		this.transitions = transitions;
	}

	public CheckoutStepValidator getCheckoutStepValidator()
	{
		return checkoutStepValidator;
	}

	public void setCheckoutStepValidator(final CheckoutStepValidator checkoutStepValidator)
	{
		this.checkoutStepValidator = checkoutStepValidator;
	}

	public CheckoutGroup getCheckoutGroup()
	{
		return checkoutGroup;
	}

	@Required
	public void setCheckoutGroup(final CheckoutGroup checkoutGroup)
	{
		this.checkoutGroup = checkoutGroup;
	}

	public String getProgressBarId()
	{
		return progressBarId;
	}

	@Required
	public void setProgressBarId(final String progressBarId)
	{
		this.progressBarId = progressBarId;
	}
}
