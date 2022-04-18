<template>
	<div class="about">
		<v-header/>
		<v-sidebar/>
		<div class="content-box" :class="{ 'content-collapse': collapse }">
			<v-tags></v-tags>
			<div class="content">
				<router-view v-slot="{ Component }">
					<transition name="move" mode="out-in">
						<keep-alive :include="tagsList">
							<component :is="Component"/>
						</keep-alive>
					</transition>
				</router-view>
			</div>
		</div>
<!--		<router-view/>-->
	</div>
</template>

<script>
import vHeader from "./common/Header";
import vSidebar from "./common/Sidebar";
import vTags from "./common/Tags";

export default {
	name: "Home",
	components: {
		vHeader,
		vSidebar,
		vTags
	},
	computed: {
		tagsList() {
			return this.$store.state.tagsList.map(item => item.name);
		},
		collapse() {
			return this.$store.state.collapse;
		}
	}
}
</script>

<style scoped>

</style>
