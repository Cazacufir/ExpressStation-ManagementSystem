<template>
	<view class="container">
		<div class="card header">
			<u-text text="已签收" bold="true" size="17"></u-text>
			<u-text text="订单编号: 123" size="13" color="gray"></u-text>
			<u-text text="下单时间: 2024-03-27" size="13" color="gray"></u-text>
		</div>

		<div class="card header">
			<u-text text="中通快递" bold="true"></u-text>
			<u-text text="快递编号: 321"></u-text>
		</div>

		<div class="card header">
			<div class="parcelCard">
				<div class="right">
					<u-text text="2024-03-27" color="#0165fe"></u-text>
					<u-text text="快件已接收"></u-text>
				</div>

				<div class="left">
					<u-icon name="arrow-right" color="#0165fe"></u-icon>
				</div>
			</div>
		</div>

		<div class="card">
			<div style="display: flex; gap: 10rpx;">
				<span>
					<u-button type="primary" text="寄"></u-button>
				</span>

				<span style="addressBar">
					<div>
						<u-text text="收件人" bold="true" size="18"></u-text>
					</div>

					<div>
						<u-text text="123" bold="true"></u-text>
					</div>

				</span>
				<div>
					<u-text text="send.address"></u-text>
				</div>
			</div>

			<div style="display: flex; gap: 10rpx;">
				<span>
					<u-button type="primary" text="收" color="#0ab99c"></u-button>
				</span>

				<span style="addressBar">
					<div>
						<u-text text="收件人" bold="true" size="18"></u-text>
					</div>

					<div>
						<u-text text="123" bold="true"></u-text>
					</div>

				</span>
				<div>
					<u-text text="send.address"></u-text>
				</div>
			</div>

		</div>

		<div class="card" style="display: flex;gap: 5rpx">
			<div>
				<u-text text="已支付运费" bold="true"></u-text>
			</div>
			<div>
				<u-text text="7" mode="price" color="#0165fe"></u-text>
			</div>

		</div>
	</view>
</template>

<script setup>

</script>

<style lang="scss" scoped>
	.container {
		display: flex;
		flex-direction: column;
		align-items: center;
		gap: 20rpx;
		height: 100%;
		background-color: #ebebef;
		padding: 20rpx;
	}

	.card {
		width: 90%;
		background-color: white;
		border-radius: 40rpx;
		padding: 20rpx;
	}

	.header {
		display: flex;
		flex-direction: column;
		align-items: center;
		gap: 10rpx;
	}

	.parcelCard {
		border-radius: 20rpx;
		border: 1px soild #c8dbfc;
		background-color: #f5f7fe;
		display: flex;
		justify-content: space-between;
		align-items: center;
		padding: 20rpx;
		width: 90%;
	}

	.addressBar {
		display: flex;
		gap: 5rpx;
		// align-items: center;
	}
</style>