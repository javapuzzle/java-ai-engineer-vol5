package com.acme.retail.architecture.ch08;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class DeploymentCostCalculatorTest {

    private final DeploymentCostCalculator calculator = new DeploymentCostCalculator();
    private final ManagedApiPricing managed = new ManagedApiPricing(new BigDecimal("0.002"));
    private final SelfHostedPricing selfHosted = new SelfHostedPricing(new BigDecimal("2000.00"));

    @Test
    void computesTheExactBreakEvenVolumeFromRealPricingInputs() {
        long breakEven = calculator.breakEvenRequestsPerMonth(managed, selfHosted);

        assertThat(breakEven).isEqualTo(1_000_000L);
    }

    @Test
    void recommendsTheManagedApiBelowTheBreakEvenVolume() {
        DeploymentRecommendation recommendation = calculator.recommend(managed, selfHosted, 500_000L);

        assertThat(recommendation.recommended()).isEqualTo(DeploymentOption.MANAGED_API);
        assertThat(recommendation.managedMonthlyCost()).isEqualByComparingTo("1000.000");
        assertThat(recommendation.selfHostedMonthlyCost()).isEqualByComparingTo("2000.00");
    }

    @Test
    void recommendsSelfHostedAboveTheBreakEvenVolume() {
        DeploymentRecommendation recommendation = calculator.recommend(managed, selfHosted, 2_000_000L);

        assertThat(recommendation.recommended()).isEqualTo(DeploymentOption.SELF_HOSTED);
        assertThat(recommendation.managedMonthlyCost()).isEqualByComparingTo("4000.000");
    }

    @Test
    void aTieAtExactlyTheBreakEvenVolumeFavorsTheManagedApi() {
        DeploymentRecommendation recommendation = calculator.recommend(managed, selfHosted, 1_000_000L);

        assertThat(recommendation.managedMonthlyCost()).isEqualByComparingTo(recommendation.selfHostedMonthlyCost());
        assertThat(recommendation.recommended()).isEqualTo(DeploymentOption.MANAGED_API);
    }

    @Test
    void roundsANonExactBreakEvenVolumeUpToTheNextWholeRequest() {
        ManagedApiPricing preciseManaged = new ManagedApiPricing(new BigDecimal("0.0007"));
        SelfHostedPricing preciseSelfHosted = new SelfHostedPricing(new BigDecimal("1500.00"));

        long breakEven = calculator.breakEvenRequestsPerMonth(preciseManaged, preciseSelfHosted);

        // 1500.00 / 0.0007 = 2,142,857.142857... -> rounds up to the next whole request.
        assertThat(breakEven).isEqualTo(2_142_858L);
    }

    @Test
    void rejectsNonPositivePricingInputs() {
        org.assertj.core.api.Assertions.assertThatThrownBy(() -> new ManagedApiPricing(BigDecimal.ZERO))
                .isInstanceOf(IllegalArgumentException.class);
        org.assertj.core.api.Assertions.assertThatThrownBy(() -> new SelfHostedPricing(new BigDecimal("-1")))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void rejectsANegativeProjectedVolume() {
        org.assertj.core.api.Assertions.assertThatThrownBy(
                        () -> calculator.recommend(managed, selfHosted, -1))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
